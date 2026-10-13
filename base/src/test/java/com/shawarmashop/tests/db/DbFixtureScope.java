package com.shawarmashop.tests.db;

import lombok.RequiredArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;

@RequiredArgsConstructor
public class DbFixtureScope {

    private final Jdbc jdbc;

    private final Deque<Cleanup> tracked = new ArrayDeque<>();

    /**Отслеживание определенных действий из таблицы БД*/
    public void track(String description, Runnable cleanup) {
        tracked.push(new Cleanup(description, cleanup));
    }

    /**Удаление записи по id*/
    public void track(String table, long id) {
        track(table + " id=" + id,
                () -> jdbc.update(
                        "DELETE FROM " + table + " WHERE id = ?",
                        id
                )
        );
    }
/**В обратном порядке достает все действия, к-е нужно выполнить
 * и будет вызываться ч/з JunitExtention после завершения всех тестов*/
    public void cleanup(){
        while (!tracked.isEmpty()) {
            Cleanup pop = tracked.pop();
            try {
                pop.getAction().run();
            } catch (RuntimeException e) {
                System.out.println("DbFixtureScope cleanup упал для " + pop.getDescription() + "\n" + e.getMessage());
            }
        }
    }

    /**Размер обратной очереди*/
    public int size(){
        return tracked.size();
    }
}
