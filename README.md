# java-explore-with-me
Template repository for ExploreWithMe project.
https://github.com/PapaG2015/java-explore-with-me/pull/2

Разработана фича "комментарии"
1) добавление комментария к событию:
POST /users/{userId}/events/{eventId}/comments
    RequestBody: NewCommentDto
2) изменение комментария пользователем
PATCH /users/{userId}/events/{eventId}/comments
Изменять комментарий можно, селм с момента опубликования прошло не более 10 мин.
   RequestBody: UpdateCommentDto
3) удаление комментария пользователя
DELETE /users/{userId}/events/{eventId}/comment/{comId}
4) получение всех комментариев пользователя
GET /users/{userId}/comments
5) получение всех комментариев события
GET /users/{userId}//events/{eventId}/comments
