/**
 * Class that represents an item of a user list.
 */
export class ListItem {
    id: string;
    poster: string;
    title: string;
    status: string;
    type: string;

    constructor(id: string, poster: string, title: string, status: string, type: string) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.status = status;
        this.type = type;
    }

}

/**
 * Class that represents the list of a user.
 * @see {@link ListItem}
 */
export class ItemUserList {

    list: ListItem[] = [];

    errorMessage: string = '';

    /**
     * Add an item to the list.
     * @param item the item to add.
     */
    addToList(item: ListItem) {
        this.list.push(item);
    }

    /**
     * Get the list of items.
     * @returns the list of items.
     */
    getUserList() {
        return this.list;
    }

    /**
     * Clear the list of items.
     */
    clearUserList() {
        this.list = [];
    }

    /**
     * Get the error message.
     * @returns the error message.
     */
    getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Set the error message.
     * @param message the error message.
     */
    setErrorMessage(message: string) {
        this.errorMessage = message;
    }

    /**
     * Get the list of completed items.
     * @returns the list of completed items.
     */
    getCompletedList() {
        return this.list.filter(item => item.status === 'completed');
    }

    /**
     * Get the list of watching items.
     * @returns the list of watching items.
     */
    getWatchingList() {
        return this.list.filter(item => item.status === 'watching');
    }

    /**
     * Get the list of plan to watch items.
     * @returns the list of plan to watch items.
     */
    getPlanToWatchList() {
        return this.list.filter(item => item.status === 'plan to watch');
    }

    /**
     * Get the list of dropped items.
     * @returns the list of dropped items.
     */
    getDroppedList() {
        return this.list.filter(item => item.status === 'dropped');
    }

    /**
     * Get the list of on hold items.
     * @returns the list of on hold items.
     */
    getOnHoldList() {
        return this.list.filter(item => item.status === 'on hold');
    }
}