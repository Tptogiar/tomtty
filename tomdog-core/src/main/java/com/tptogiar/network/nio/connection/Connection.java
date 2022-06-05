package com.tptogiar.network.nio.connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.channels.SelectionKey;
import java.util.Objects;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/5 - 21:09
 */

@AllArgsConstructor
@NoArgsConstructor
public class Connection {

    private SelectionKey selectionKey;

    private long deadline;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(selectionKey, that.selectionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectionKey);
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
