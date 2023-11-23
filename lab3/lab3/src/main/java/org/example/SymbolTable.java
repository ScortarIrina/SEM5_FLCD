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

    public void addToST(String term) {
        hashTable.add(term);
    }

    @Override
    public String toString() {
        return this.hashTable.toString();
    }

}
