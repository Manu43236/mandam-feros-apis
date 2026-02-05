# FEROS API Integration – Cursor Prompts

Use these prompts one at a time with Cursor. Give Cursor this repo (and the React app if it’s in another folder) plus `postman-collection.json` as context. Complete each step and test before moving to the next.

---

## Step 1: Auth API – Login and persist JWT (do this first)

Copy the block below and use it as the Cursor prompt. Attach or reference `postman-collection.json` and your React app.

```markdown
## Task: Integrate FEROS Auth API and persist JWT

**Context**
- Backend: FEROS Spring Boot API. Base URL from env (e.g. `VITE_API_BASE_URL` or `REACT_APP_API_BASE_URL`) — default `http://localhost:8081`.
- API contract: See `postman-collection.json` in this repo, folder **Auth**:
  - **POST** `{{baseUrl}}/api/auth/login` — login (JSON body).
  - **POST** `{{baseUrl}}/api/auth/refresh` — refresh token (JSON body).

**Login request**
- **Email + password:** body `{ "email": "string", "password": "string" }`.
- **Phone + PIN:** body `{ "phone": "string", "pin": "string" }`.
- Same endpoint for both; response shape is the same.

**Login response (success)**
- JSON with at least: `accessToken`, `refreshToken` (and possibly `user`, `expiresIn`, etc.). Use `accessToken` as the Bearer token for all other API calls.

**What to implement**

1. **API client**
   - Create a shared API client (e.g. axios instance or fetch wrapper) that:
     - Uses base URL from environment variable.
     - Sends `Content-Type: application/json` for JSON bodies.
   - Do not add the Bearer token in this step to the client; we will add it in a follow-up (or add it now if you prefer — see below).

2. **Auth service / API calls**
   - Implement a function that calls `POST /api/auth/login` with the chosen payload (email+password or phone+PIN from the login form).
   - Implement a function that calls `POST /api/auth/refresh` with body `{ "refreshToken": "<stored refresh token>" }`.

3. **Token persistence**
   - On successful login, persist in **localStorage** (or sessionStorage if you prefer):
     - `accessToken`
     - `refreshToken` (if returned)
   - Also hold the token in app state (e.g. React context or state management) so the UI knows the user is logged in.

4. **Login screen**
   - Wire the existing login form to the login API:
     - On submit, call the login API with form values.
     - On success: save tokens (localStorage + state), then redirect to the main app (e.g. dashboard or home).
     - On failure: show the error message from the API (e.g. "Invalid credentials") and do not redirect.

5. **Protected routes (optional but recommended)**
   - If you have a router, ensure routes that require login redirect to the login page when there is no valid token (e.g. no `accessToken` in state/localStorage).

6. **Attach token to API client (recommended)**
   - After login, all other API requests must send `Authorization: Bearer <accessToken>`.
   - Either:
     - Set the default `Authorization` header on the shared API client whenever the token is set (e.g. after login and on app init from localStorage), or
     - Add an interceptor that reads the current token from your auth state/localStorage and adds it to every request.
   - Use the same client for all future API integrations so you don’t have to add the header again.

7. **Logout**
   - Implement logout: clear tokens from localStorage and from app state, then redirect to the login page.

**Definition of done**
- User can log in with email+password (or phone+PIN) and is redirected to the app after success.
- Tokens are stored in localStorage and in app state.
- Failed login shows an error message.
- Logout clears tokens and redirects to login.
- The shared API client sends `Authorization: Bearer <accessToken>` for requests when a token is present (so ready for next steps: Clients, Users, etc.).
```

---

## Next steps (prompts to use after Step 1)

- **Step 2:** Clients API (list with search/pagination, get by ID, create, update, delete).
- **Step 3:** Users API (list with search/pagination, get by ID, create, update, etc.).
- **Step 4:** Connections API.
- **Step 5:** Vehicle Types API.
- **Step 6:** Vehicle Makes API.
- **Step 7:** Vehicle Models API.
- **Step 8:** Vehicles API.
- **Step 9:** Orders API.
- **Step 10:** Vehicle Assignments API.
- **Step 11:** Lorry Receipts API.

After you finish Step 1 and confirm it works, ask for the **Step 2 prompt** and we’ll add the next markdown block to this file.
