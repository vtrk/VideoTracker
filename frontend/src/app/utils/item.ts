/**
 * @fileoverview This file contains interfaces and classes for storing and managing items.
 */

import { strings } from "./strings";

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
 * Interface for assigning items to an ItemList.
 * @see {@link ItemList}
 * @see {@link Item}
 * 
 * @see {@link TMDBItemAssigner}
 * @see {@link KitsuItemAssigner}
 * @interface
 */
export interface ItemAssigner {
    /**
     * Assign items to an ItemList.
     * @param itemList to assign to
     * @param json JSON of items to assign (parsed)
     */
    assign(itemList: ItemList, json: any): void;
}

/**
 * Wrapper class to assign an item from the TMDB API to an ItemList.
 * @see {@link ItemList}
 * @see {@link Item}
 * @implements {ItemAssigner}
 */
export class TMDBItemAssigner implements ItemAssigner {
    /**
     * Assigns items from the TMDB API to an ItemList.
     * @param itemList to assign to
     * @param json JSON of items to assign (parsed)
     */
    assign(itemList: ItemList, json: any): void {
        json.results.forEach((element: any) => {
            let item: Item; // Movies and tv shows have a different name for the title/name field.
            if(element.title) { // If the title field exists, it is a movie.
                item = {
                    id: element.id,
                    type: strings.movie,
                    name: element.title,
                    image: strings.TMDB_poster_url + element.poster_path
                };
            }
            else
                item = {
                    id: element.id,
                    type: strings.tv,
                    name: element.name,
                    image: strings.TMDB_poster_url + element.poster_path
                };
            itemList.addItem(item);
        });
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
     * Assigns items from the Kitsu API to an ItemList.
     * @param itemList to assign to
     * @param json JSON of items to assign (parsed)
     */
    assign(itemList: ItemList, json: any): void {
        json.data.forEach((element: any) => {
            let item: Item = {
                id: element.id,
                type: element.type,
                name: element.attributes.canonicalTitle,
                image: element.attributes.posterImage.original
            };
            itemList.addItem(item);
        });
    }
}