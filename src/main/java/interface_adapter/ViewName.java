package interface_adapter;

/**
 * The type of view in the right details panel.
 * Our layout is a two panel layout: left panel shows the map, right panel shows details.
 * Since the right panel is the one that changes, this enum
 *      represents the different views that it can display.
 */
public enum ViewName {
    EMPTY,
    TRUCK_LIST,
    TRUCK_OVERVIEW,
    TRUCK_MENU,
    TRUCK_REVIEWS,
    EDIT_TRUCK
}
