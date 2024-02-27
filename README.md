Приложение ExploreWithMe (англ. «исследуй со мной»). Оно позволит пользователям делиться информацией об интересных событиях и находить компанию для участия в них
https://github.com/TURB0B0Y/java-explore-with-me/pull/4
## Endpoints
### Public: Подборки событий
- [X] GET /compilations
- [X] GET /compilations/{compId}

### Admin: Категории
- [X] POST /admin/categories
- [X] DELETE /admin/categories/{catId}
- [X] PATCH /admin/categories/{catId}

### Private: События
- [X] GET /users/{userId}/events
- [X] POST /users/{userId}/events
- [X] GET /users/{userId}/events/{eventId}
- [X] PATCH /users/{userId}/events/{eventId}
- [X] GET /users/{userId}/events/{eventId}/requests
- [X] PATCH /users/{userId}/events/{eventId}/requests

### Public: Категории
- [X] GET /categories
- [X] GET /categories/{catId}

### Admin: События
- [X] GET /admin/events
- [X] PATCH /admin/events/{eventId}

### Public: События
- [X] GET /events
- [X] GET /events/{id}

### Private: Запросы на участие
- [X] GET /users/{userId}/requests
- [X] POST /users/{userId}/requests
- [X] PATCH /users/{userId}/requests/{requestId}/cancel

### Admin: Пользователи
- [X] GET /admin/users
- [X] POST /admin/users
- [X] DELETE /admin/users/{userId}

### Admin: Подборки событий
- [X] POST /admin/compilations
- [X] DELETE /admin/compilations/{compId}
- [X] PATCH /admin/compilations/{compId}