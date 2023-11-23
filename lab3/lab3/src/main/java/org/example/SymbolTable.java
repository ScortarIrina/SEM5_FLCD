package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolTable {
    private Integer size;
    private HashTable hashTable;

    public SymbolTable(Integer size) {
        hashTable = new HashTable(size);
    }

    public boolean add(String term) {
        return hashTable.add(term);
    }

    @Override
    public String toString() {
        return this.hashTable.toString();
    }

}
