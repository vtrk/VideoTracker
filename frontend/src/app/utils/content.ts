import { strings } from "./strings";

/**
 * Generic content representation.
 */
export class Content {
    id: string;
    API: string;
    type: string;
    /**
     * Error string to display if the content could not be retrieved.
     */
    errorString: string = '';
    
    getID() {
        return this.id;
    }
    /**
     * @returns the API name from which the content was retrieved.
     */
    contentAPI(){
        return this.API;
    }
    
    /**
     * Sets the error string
     * @param errorString the error string to display if the content could not be retrieved.
     */
    setErrorString(errorString: string){
        this.errorString = errorString;
    }

    /**
     * Returns the error string.
     */
    getErrorString(){
        return this.errorString;
    }
}

/**
 * Content representation of an TMDB item.
 * 
 * This class will be fed to a function that will retrieve the content from the TMDB API and then assign the values to the fields.
 */
export class TMDBContent extends Content {

    title: string;
    description: string;
    poster: string;
    releaseDate: string;

    // Movie specific fields
    runtime: string;

    // TV Show specific fields
    seasons: string;
    episodes: string;

    constructor() {
        super();
        this.API = strings.TMDB;
    }

    /**
     * Sets the values of the fields.
     * @param id item id
     * @param title field name is 'name' for tv shows and 'title' for movies
     * @param description field name is 'overview'
     * @param poster field name is 'poster_path'
     * @param releaseDate field name is 'release_date' for movies and 'first_air_date' for tv shows
     */
    setValues(id: string, title: string, description: string, poster: string, releaseDate: string, type: string, runtime?: string, seasons?: string, episodes?: string){
        this.id = id;
        this.title = title;
        this.description = description;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.type = type;

        if(runtime)
            this.runtime = runtime;
        if(seasons)
            this.seasons = seasons;
        if(episodes)
            this.episodes = episodes;

    }

}

/**
 * Content representation of a Kitsu item.
 * 
 */
export class KitsuContent extends Content {

    title: string;
    synopsis: string;
    poster: string;
    episodeCount: string;
    episodeLength: string;
    showType: string;
    status: string;
    startDate: string;
    endDate: string;
    ageRating: string;
    genres: string;

    constructor() {
        super();
        this.API = strings.KITSU;
        this.type = strings.anime;
    }

    setValues(id: string, title: string, synopsis: string, poster: string, episodeCount: string, episodeLength: string, showType: string, status: string, startDate: string, endDate: string, ageRating: string, genres: string){
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.poster = poster;
        this.episodeCount = episodeCount;
        this.episodeLength = episodeLength;
        this.showType = showType;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ageRating = ageRating;
    }
}