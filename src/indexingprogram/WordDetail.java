/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexingprogram;

/**
 *
 * @author 1
 */
public class WordDetail {
    private int index;
    private int lineNumber;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public WordDetail(int index, int lineNumber) {
        this.index = index;
        this.lineNumber = lineNumber;
    }
}
