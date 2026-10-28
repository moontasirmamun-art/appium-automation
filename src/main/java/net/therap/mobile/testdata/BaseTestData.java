package net.therap.mobile.testdata;

import com.univocity.parsers.annotations.Parsed;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BaseTestData {

    @Parsed(field = "Test Case ID", defaultNullRead = "")
    private String testCaseId;

    @Parsed(field = "Test Case Description", defaultNullRead = "")
    private String testCaseDescription;

    @Parsed(field = "Login Name", defaultNullRead = "")
    private String loginName;

    @Parsed(field = "Password", defaultNullRead = "")
    private String password;

    @Parsed(field = "Provider Code", defaultNullRead = "")
    private String providerCode;

    @Parsed(field = "Error Message", defaultNullRead = "")
    private String errorMessage;
}
