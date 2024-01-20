class Review{
  id: string;
  vote: string;
  user_comment: string;
  username: string;
  id_user: string;
  id_content: string;
}

export class ReviewList{
  list: Review[];

  error_string: string = "";

  constructor(){
    this.list = [];
  }

  add(id: string, vote: string, user_comment: string, username: string, id_user: string, id_content: string){
    var item = new Review();
    item.id = id;
    item.vote = vote;
    item.user_comment = user_comment;
    item.username = username;
    item.id_user = id_user;
    item.id_content = id_content;
    this.list.push(item);
  }

  getReviewList(){
    return this.list;
  }

  setErrorString(error_string: string){
    this.error_string = error_string;
  }

  getErrorString(){
    return this.error_string;
  }
}
