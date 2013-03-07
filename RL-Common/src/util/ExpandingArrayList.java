package util;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class ExpandingArrayList<E> extends ArrayList<E> {

    public ExpandingArrayList(int i) {
        super(i);
    }

    public ExpandingArrayList() {
    }

    public ExpandingArrayList(Collection<? extends E> clctn) {
        super(clctn);
    }
    
    @Override
    public E set(int i, E e) {
        if (i > super.size()) {
            fill(i + 1);
        }
        return super.set(i, e);
    }

    @Override
    public void add(int i, E e) {
        if (i > super.size()) {
            fill(i);
        }
        super.add(i, e);
    }

    private void fill(int newSize) {
        for (int i = super.size(); i < newSize; i++) {
            super.add(null);
        }
    }
}
