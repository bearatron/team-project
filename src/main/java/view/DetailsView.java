package view;

import interface_adapter.details.DetailsViewModel;
import interface_adapter.details.DetailsViewModelListener;

import java.awt.*;
import javax.swing.*;

public class DetailsView extends JPanel implements DetailsViewModelListener {
    private final CardLayout cardLayout;
    // parent container where view switching happens
    private final JPanel viewContainer;
    private DetailsViewModel detailsViewModel;

    public DetailsView(
            JPanel viewContainer,
            CardLayout cardLayout,
            DetailsViewModel detailsViewModel
    ) {
        this.viewContainer = viewContainer;
        this.cardLayout = cardLayout;
        this.detailsViewModel = detailsViewModel;
        this.detailsViewModel.addListener(this);
    }

    @Override
    public void stateChanged() {
        cardLayout.show(
                viewContainer,
                detailsViewModel.getState().getCurrentViewName().toString()
        );
    }

}
