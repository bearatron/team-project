package use_case.add_menu_item;

/**
 * Output boundary for the Add Menu Item use case.
 */
public interface AddMenuItemOutputBoundary {

    /**
     * Prepares the success view.
     *
     * @param outputData the added item's details
     */
    void prepareSuccessView(AddMenuItemOutputData outputData);

    /**
     * Prepares the failure view.
     *
     * @param errorMessage why the item could not be added
     */
    void prepareFailView(String errorMessage);
}