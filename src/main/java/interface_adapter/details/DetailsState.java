package interface_adapter.details;

/**
 * The state for the Details View Model.
 */
public class DetailsState {
    private DetailsViewName currentViewName = DetailsViewName.TRUCK_LIST;

    public DetailsViewName getCurrentViewName() {
        return currentViewName;
    }

    public void setCurrentViewName(DetailsViewName newViewName) {
        this.currentViewName = newViewName;
    }

}
