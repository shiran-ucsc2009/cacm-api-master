package com.kpmg.cacm.api.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.exceptions.SqlGrammarException;
import com.kpmg.cacm.api.model.AllowedTable;
import com.kpmg.cacm.api.repository.spring.ClientDataRepository;
import com.kpmg.cacm.api.service.AllowedTableService;
import com.kpmg.cacm.api.service.ClientDataService;
import com.kpmg.cacm.api.util.RsaCypher;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientDataServiceImpl implements ClientDataService {

    private final ClientDataRepository clientDataRepository;

    private final List<String> allowedTables;

    @Value("${cacm.config.client.data.customer-id-field}")
    private String clientCustomerIdField;

    @Value("${cacm.config.client.data.customer-name-field}")
    private String clientCustomerNameField;

    @Autowired
    public ClientDataServiceImpl(
            final ClientDataRepository clientDataRepository,
            final AllowedTableService allowedTableService,
            final RsaCypher rsaCypher) {
        this.clientDataRepository = clientDataRepository;
        this.allowedTables = allowedTableService.findAll().stream().map(AllowedTable::getName)
                .map(rsaCypher::decrypt).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> executeQuery(final String sql) {
        return this.executeQuery(sql, null);
    }

    @Override
    public List<Map<String, Object>> executeQuery(final String sql, final Map<String, Object> subQueryParams) {
        final String formattedSql = new StrSubstitutor(subQueryParams).replace(sql);
        final Statement statement = this.parseSql(formattedSql);
        this.checkStatementType(statement);
        this.checkTableNames(statement);
        final List<Map<String, Object>> result = this.clientDataRepository.executeQuery(formattedSql);
        if (subQueryParams == null) {
            if(!result.isEmpty()) {
                this.checkCustomerFields(result);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> executeClientQuery(String sql) {
        return this.executeClientQuery(sql, null);
    }

    @Override
    public List<Map<String, Object>> executeClientQuery(String sql, Map<String, Object> subQueryParams) {
        final String formattedSql = new StrSubstitutor(subQueryParams).replace(sql);
        final Statement statement = this.parseSql(formattedSql);
        final List<Map<String, Object>> result = this.clientDataRepository.executeQuery(formattedSql);
        return result;
    }

    private void checkStatementType(final Statement statement) {
       if(!(statement instanceof Select)) {
           throw new SqlGrammarException("SQL should be a SELECT statement");
       }
    }

    private Statement parseSql(final String sql) {
        try {
            return CCJSqlParserUtil.parse(sql
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
            );
        } catch (final JSQLParserException exception) {
            ClientDataServiceImpl.log.error(exception.getLocalizedMessage(), exception);
            throw new SqlGrammarException("Invalid SQL syntax");
        }
    }

    private void checkTableNames(final Statement statement) {
        final boolean invalid = new TablesNamesFinder()
            .getTableList(statement)
            .stream()
            .anyMatch(tableName -> !this.allowedTables.contains(tableName));
        if(invalid) {
            throw new SqlGrammarException("Unauthorized SQL Query.Please Contact Support");
        }
    }

    private void checkCustomerFields(final List<Map<String, Object>> result) {
        if(!result.get(0).containsKey(this.clientCustomerIdField) ||
            !result.get(0).containsKey(this.clientCustomerNameField)) {
            throw new SqlGrammarException("SQL query should contain " +
            this.clientCustomerIdField + " and " + this.clientCustomerNameField + " fields");
        }
    }
}
