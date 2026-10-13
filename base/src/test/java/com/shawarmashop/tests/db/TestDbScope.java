package com.shawarmashop.tests.db;

public class TestDbScope {

    /**На каждый поток создает трекер удаленных записей и сама за собой все подчищает*/
    private final static ThreadLocal<DbFixtureScope> SCOPE =
            ThreadLocal.withInitial(() -> new DbFixtureScope(TestDatabase.jdbc()));

    public static DbFixtureScope current(){
        return SCOPE.get();
    }

    public static void cleanup(){
        SCOPE.get().cleanup();
        SCOPE.remove();
    }
}
