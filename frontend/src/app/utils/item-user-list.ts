/**
 * Class that represents an item of a user list.
 */
class ListItem {
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
}