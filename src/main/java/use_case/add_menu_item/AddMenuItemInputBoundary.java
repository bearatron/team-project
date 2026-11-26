package use_case.add_menu_item;

/**
 * Input boundary for the Add Menu Item use case.
 */
public interface AddMenuItemInputBoundary {

    /**
     * Executes the Add Menu Item use case.
     *
     * @param inputData the input data
     */
    void execute(AddMenuItemInputData inputData);
}
