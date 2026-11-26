package view;

import interface_adapter.test.TestViewModel;
import interface_adapter.test.TestViewModelListener;

import java.awt.*;
import javax.swing.*;

public class TestView extends JPanel implements TestViewModelListener {
    private final JLabel numLabel;
    private TestViewModel testViewModel;

    public TestView() {
        testViewModel = new TestViewModel();
        testViewModel.addListener(this);

        setLayout(new BorderLayout());
        add(new JLabel("Test view", JLabel.CENTER), BorderLayout.CENTER);
        numLabel = new JLabel(String.valueOf(testViewModel.getNum()), JLabel.CENTER);
        add(numLabel, BorderLayout.CENTER);
    }

    @Override
    public void stateChanged() {
        numLabel.setText(String.valueOf(testViewModel.getNum()));
    }
}
