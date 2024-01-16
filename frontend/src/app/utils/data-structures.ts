/**
 * Contains various JSON interfaces and classes that represent data structures for client-server communication.
**/

/**
 * The response of the server from /api endpoint.
 * 
 * JSON response format:
 * 
 * * {"Version":"version_number","API":"api_name"}
 * 
 * @param Version The version string of the server.
 * 
 * @param API The API string of the server.
 * 
 * @see {@link ServerService}
 */
export interface ServerAPIResponse {
    Version: string;
    API: string;
}

/**
 * The body of a search query.
 * 
 * @param type The type of search query.
 * @param query The query string.
 * @param args The arguments for the query.
 */
export class SearchBody {

    /**
     * Constructs a new search body.
     * @param type 
     * @param query 
     * @param args 
     */
    constructor(
        public type: string,
        public query: string,
        public args: Map<string, string>
    ) {}

    /**
     * Returns a JSON string of the body for a search query.
     */
    getJSONBody() {
        return JSON.stringify({
            type: this.type,
            query: this.query,
            args: Object.fromEntries(this.args)
        });
    }
}