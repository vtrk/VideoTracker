/**
 * @fileoverview This file contains interfaces and classes for storing and managing items.
 */

/**
 * Interface for storing an item.
 * An item represents a piece of content that is fetched from the /trending or /search endpoints.
 */
export interface Item {
    id: number;
    type: string;
    name: string;
    image: string;
}

/**
 * Class for storing a list of items.
 * These items represent content that is fetched from the /trending or /search endpoints.
 * @see {@link Item}
 */
export class ItemList {
    /**
     * Title of the list.
     * 
     * This is used to display the title of the list on the page.
     */
    title: string = '';

    /**
     * List of items.
     */
    items: Item[] = [];
    
    constructor() {}

    /**
     * Adds an item to the list.
     * @param item to add
     */
    addItem(item: Item) {
        this.items.push(item);
    }

    /**
     * Gets the list of items.
     * @returns the list of items.
     */
    getItems() {
        return this.items;
    }

    /**
     * Clears the list of items.
     */
    clearItems() {
        this.items = [];
    }

    /**
     * Sets the title of the list.
     * @param title to set
     */
    setTitle(title: string) {
        this.title = title;
    }

    /**
     * Gets the title of the list.
     * @returns the title of the list
     */
    getTitle() {
        return this.title;
    }
}

/**
 * Interface for assigning an item to an ItemList.
 * @see {@link ItemList}
 * @see {@link Item}
 * 
 * @see {@link TMDBItemAssigner}
 * @see {@link KitsuItemAssigner}
 * @interface
 */
export interface ItemAssigner {
    /**
     * Assigns an item to an ItemList.
     * @param itemList to assign to
     * @param element to assign
     */
    assign(itemList: ItemList, element: any): void;
}

/**
 * Wrapper class to assign an item from the TMDB API to an ItemList.
 * @see {@link ItemList}
 * @see {@link Item}
 * @implements {ItemAssigner}
 */
export class TMDBItemAssigner implements ItemAssigner {
    /**
     * Assigns an item from the TMDB API to an ItemList.
     * @param itemList to assign to
     * @param element to assign (parsed JSON)
     */
    assign(itemList: ItemList, element: any): void {
        throw new Error("Method not implemented.");
    }
}

/**
 * Wrapper class to assign an item from the Kitsu API to an ItemList.
 * @see {@link ItemList}
 * @see {@link Item}
 * @implements {ItemAssigner}
 */
export class KitsuItemAssigner implements ItemAssigner {
    /**
     * Assigns an item from the Kitsu API to an ItemList.
     * @param itemList to assign to
     * @param element to assign (parsed JSON)
     */
    assign(itemList: ItemList, element: any): void {
        let item: Item = {
            id: element.id,
            type: element.type,
            name: element.attributes.canonicalTitle,
            image: element.attributes.posterImage.original
        };
        itemList.addItem(item);
    }
}