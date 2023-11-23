package org.example;

import lombok.Data;

@Data
public class SymbolTable {
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
