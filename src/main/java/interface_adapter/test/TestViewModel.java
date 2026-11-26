package interface_adapter.test;

import interface_adapter.details.DetailsViewName;

import java.util.ArrayList;
import java.util.List;

public class TestViewModel {
    private int num;
    public static final DetailsViewName VIEW_NAME = DetailsViewName.TEST;
    private final List<TestViewModelListener> listeners = new ArrayList<>();

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public void addListener(TestViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (TestViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
