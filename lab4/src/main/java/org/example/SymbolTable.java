package org.example;

import lombok.Data;

@Data
public class SymbolTable {
    private HashTable hashTable;

    public SymbolTable(Integer size) {
        hashTable = new HashTable(size);
    }

    public void addToST(String term) {
        hashTable.add(term);
    }

    @Override
    public String toString() {
        return this.hashTable.toString();
    }

}
