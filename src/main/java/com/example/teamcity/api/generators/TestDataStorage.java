package com.example.teamcity.api.generators;

import java.util.ArrayList;
import java.util.List;

// It`s a singleton (implemented by closed constructor)
public class TestDataStorage {

    private static TestDataStorage testDataStorage;

    private List<TestData> testDataList;

    private TestDataStorage() {
        this.testDataList = new ArrayList<>();
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    // we don`t pass test data by default
    public TestData addTestData() {
        var testData = TestDataGenerator.generate();
        addTestData(testData);
        return testData;
    }

    // we pass test data
    public TestData addTestData(TestData testData) {
        getStorage().testDataList.add(testData);
        return testData;

    }

    public void delete() {
        testDataList.forEach(TestData::delete);


    }
}
