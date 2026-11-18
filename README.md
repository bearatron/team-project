# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.

## Team Information

### Team Members
- Ethan Diep
- Clelia Troisi
- Gilbert Lauridsen
- Joshua Morton
- Soham Dhar

---

## Project Description

### Domain

Google Maps for food trucks on the UofT St. George campus

---

## Features

Informal list of features. Details specified in [user stories](#user-stories) and [use cases](#use-cases).

### Required (MVP)
1. log in/log out - reviewer or food truck owner
2. sign up - account creation
    - a food truck is automatically associated with an owner account when it is made
3. see where food trucks are on a map and pick one to view info
    - custom API for location of food trucks
4. view details of a food truck: name, cuisine, price range, menu, images, reviews
5. view summary of a food truck's reviews (LLM api)
6. post a review to a food truck with rating or images
    - rating out of 5
    - optional written review
    - categories: overall, service, food quality, value for money
    - sort reviews by rating category
7. food truck owners can change their store's location on the map
8. food truck owners can change their filters (cuisine type, price range)
9. food truck owners can reply to reviews
10. food truck owners can import reviews from Google Maps API

### Stretch
1. view most active/popular users based on reviewers
2. followers
3. see user's review history
4. report a food truck
5. analytics for food truck owners (what time views were, how many views)
6. search for a food you want
    - gather all menus and filter (preference, price)
7. user's list of favourite food trucks
8. ratings out of half stars
9. food restrictions on food trucks
10. edit or delete reviews (reviewers) or replies (owners)

## User Stories
- a "user" is anyone using the app, whether or not they are logged in.
- a "reviewer" is a user logged in to a reviewer account.
    - can post their own reviews
- an "owner" is a user logged in to an owner account
    - can register and manage a food truck
    - can reply to reviews on their food truck
    - cannot post reviews
- every account is either a reviewer or an owner account and can never be both

MVP user stories listed below

### Authentication
- Sign up (new reviewer): As a new reviewer, I want to create an account, so I can review food trucks.
- Sign up (new owner): As a new food truck owner, I want to create an owner account, so I can register and manage my food truck.
- Login: As a reviewer or owner, I want to log in, so I can access my account and data.
- Logout: As a reviewer or owner, I want to log out, so my account stays secure on shared devices.

### Map and Food Truck Overview
- As a user, I want to see food trucks on a map of campus, so I can find one close to where I am. 
- As a user, I want to view a food truck's details, so I can decide whether to go there.

### Reviews

Writing and editing reviews
- As a reviewer, I want to review a food truck, so other users can learn from my experience.
- As a reviewer, I want to attach images to my review, so I can show what I ordered.
- As a reviewer, I want to rate a food truck in specific rating categories, so my feedback is more detailed.
- (stretch): As a reviewer, I want to edit my review, so I can correct it.
- (stretch): As a reviewer, I want to delete my review, so I can remove it.

Viewing reviews
- As a user, I want to view a food truck's reviews, so I can see what other people think.
- As a user, I want to sort a food truck's reviews by a specific rating category, so I can focus on what matters most to me.
- As a user, I want to see a summary of a food truck's reviews, so I can understand what people think without reading them all.

### Owners
- As an owner, I want to update my truck's location, so users can find me when I move.
- As an owner, I want to edit my truck's cuisine and price range, so users see accurate information about my food truck.
- As an owner, I want to import my truck's Google Maps reviews, so my page has reviews from the start.

Replying to reviews
- As an owner, I want to reply to a review of my truck, so I can respond to feedback and show users I’m engaged.
- (stretch): As an owner, I want to edit my reply to a review, so I can correct or update my response.
- (stretch): As an owner, I want to delete my reply to a review, so I can remove a response I no longer want to show.

Menu
- As an owner, I want to add items to my menu, so users can see what I offer and what it costs.
- As an owner, I want to edit a menu item, so my menu and prices stay accurate.
- As an owner, I want to remove a menu item, so users don’t see things I no longer sell.

Photo upload
- As an owner, I want to upload photos of my truck, so users can see what I offer.
- As an owner, I want to remove a photo of my truck, so users only see current photos.

---

## Use Cases

### Sign up as a reviewer

- **Actor:** User (not logged in)
- **Preconditions:** None
- **Main flow:**
    1. User chooses "Sign up as reviewer".
    2. User enters a username, password, and password confirmation.
    3. System validates the input, creates a reviewer account, and logs the user in.
- **Alternate/error flows:**
    - The username is already taken (as a reviewer or an owner account), so the system shows an error.
    - The passwords do not match, or a field is empty or fails the length rules, so the system shows an error.
- **Postconditions:** A reviewer account exists and a session is active.

### Sign up as an owner and register a truck

- **Actor:** User (not logged in)
- **Preconditions:** None
- **Main flow:**
    1. User chooses "Sign up as owner".
    2. User enters the account credentials and truck details: name, cuisine, price range, and initial location on the campus map.
    3. System validates the input, creates the owner account and the truck, and logs the owner in.
- **Alternate/error flows:**
    - Same credential errors as "Sign up as a reviewer".
    - The truck name is empty or the location is not on campus, so the system shows an error.
- **Postconditions:** An owner account is linked to exactly one new truck, and a session is active.

### Log in

- **Actor:** Reviewer or owner
- **Preconditions:** The user has an account and is not logged in.
- **Main flow:**
    1. User enters a username and password.
    2. System verifies the credentials and starts a session.
    3. System routes the user to the view for their account type.
- **Alternate/error flows:** Unknown username or wrong password gives the same generic error message, so it does not reveal which was wrong.
- **Postconditions:** A session is active with the account type known.

### Log out

- **Actor:** Reviewer or owner
- **Preconditions:** Logged in
- **Main flow:** User chooses "Log out", the system clears the session, and the app returns to the logged-out view.
- **Postconditions:** No session is active. Reviewer and owner actions are unavailable until the next login.

### View trucks on the campus map

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User opens the map.
    2. System loads all trucks and displays a marker at each truck's current location.
    3. User pans and zooms the map.
- **Alternate/error flows:**
    - There are no trucks, so the map shows with a "no trucks yet" message.
    - Map tiles fail to load, so the system shows an error.
- **Postconditions:** None (read-only).

### View a truck's details

- **Actor:** User
- **Preconditions:** The truck exists.
- **Main flow:**
    1. User selects a truck marker or list entry.
    2. System shows the truck's name, cuisine, price range, location, photos, menu, and average ratings.
- **Alternate/error flows:** The truck has no photos or no menu, so the system shows an empty-state message for that section.
- **Postconditions:** None (read-only).

### Post a review

- **Actor:** Reviewer
- **Preconditions:** Logged in as a reviewer. The reviewer has not already reviewed this truck.
- **Main flow:**
    1. Reviewer opens a truck and chooses "Write a review".
    2. Reviewer rates the truck 1-5 in each rating category and optionally enters text.
    3. System validates the input and saves the review with the reviewer, truck, and timestamp.
    4. System updates the truck's average ratings and shows the new review.
- **Alternate/error flows:**
    - A rating category is missing or outside 1-5, so the system shows an error.
    - The text exceeds the maximum length, so the system shows an error.
    - The reviewer has already reviewed this truck, so the system rejects the review.
- **Postconditions:** The review is persisted and the truck's aggregate ratings are recalculated.

### Attach images to a review

- **Actor:** Reviewer
- **Preconditions:** Logged in as a reviewer. The reviewer is composing a review ("Post a review").
- **Main flow:**
    1. Reviewer chooses "Add image" and selects one or more image files.
    2. System validates file type and size, and shows a preview.
    3. On submit, the system stores the images and links them to the review.
- **Alternate/error flows:**
    - The file type is unsupported, the file exceeds the size limit, or the image count exceeds the maximum, so the system rejects that file and keeps the rest.
    - The file cannot be read or stored, so the system shows an error and the review is still submittable without it.
- **Postconditions:** The review has zero or more linked images.

### View a truck's reviews

- **Actor:** User
- **Preconditions:** The truck exists.
- **Main flow:** User opens the truck's reviews. System lists each review with the reviewer's username, category ratings, text, images, date, and owner reply (if any), newest first by default.
- **Alternate/error flows:** There are no reviews, so the system shows an empty-state message.
- **Postconditions:** None (read-only).

### Sort reviews by rating category

- **Actor:** User
- **Preconditions:** The truck has at least one review.
- **Main flow:**
    1. User selects a rating category and a direction (highest or lowest first).
    2. System reorders the reviews by that category.
- **Alternate/error flows:** Ties are broken by newest first.
- **Postconditions:** None (the sort is view state only).

### View a review summary

- **Actor:** User
- **Preconditions:** The truck exists.
- **Main flow:**
    1. User opens the truck's summary section.
    2. System sends the truck's review text to the LLM API and displays the returned summary.
- **Alternate/error flows:**
    - There are too few reviews to summarize, so the system shows a message in place of a summary.
    - The API call fails or times out, so the system shows an error and the rest of the page still works.
- **Postconditions:** None. Open design question: cache the summary and regenerate it when a review is added, edited, or deleted.

### Update truck location

- **Actor:** Owner
- **Preconditions:** Logged in as an owner.
- **Main flow:**
    1. Owner opens the location editor on the map.
    2. Owner selects a new position.
    3. System validates that the position is within campus bounds, saves it, and moves the marker.
- **Alternate/error flows:** The position is outside campus bounds, so the system shows an error and keeps the old location.
- **Postconditions:** The truck's location is updated and visible to all users.

### Edit cuisine and price range

- **Actor:** Owner
- **Preconditions:** Logged in as an owner.
- **Main flow:** Owner edits the cuisine and/or price range, and the system validates and saves the change.
- **Alternate/error flows:** The cuisine is empty or the price range is not one of the allowed values, so the system shows an error.
- **Postconditions:** The truck details are updated.

### Import Google Maps reviews

- **Actor:** Owner
- **Preconditions:** Logged in as an owner.
- **Main flow:**
    1. Owner chooses "Import Google Maps reviews" and identifies the truck's Google Maps listing.
    2. System fetches the listing's reviews through the external API.
    3. System saves them to the truck, labelled as imported with their original author name, rating, text, and date.
- **Alternate/error flows:**
    - The listing is not found, so the system shows an error.
    - The API fails or is rate-limited, so nothing is imported.
    - Reviews were already imported, so the system skips duplicates.
- **Postconditions:** The truck has imported reviews, shown separately from native reviews. Open design question: how a single Google rating maps onto the four rating categories.

### Upload a truck photo

- **Actor:** Owner
- **Preconditions:** Logged in as an owner.
- **Main flow:** Owner selects an image file, the system validates the type and size, stores it, and adds it to the truck's photo gallery.
- **Alternate/error flows:** Unsupported type, oversize file, photo limit reached, or storage failure each give an error and no photo is added.
- **Postconditions:** The photo appears on the truck's details page.

### Remove a truck photo

- **Actor:** Owner
- **Preconditions:** Logged in as an owner, and the photo belongs to the owner's truck.
- **Main flow:** Owner selects a photo, chooses "Remove", and confirms. The system deletes it from the gallery.
- **Alternate/error flows:** The owner cancels the confirmation, so nothing changes.
- **Postconditions:** The photo no longer appears.

### Add a menu item

- **Actor:** Owner
- **Preconditions:** Logged in as an owner.
- **Main flow:** Owner enters an item name and price (and optionally a description), and the system validates it and adds the item to the truck's menu.
- **Alternate/error flows:** The name is empty, the price is non-numeric, zero or negative, or the name duplicates an existing item, so the system shows an error.
- **Postconditions:** The item appears on the truck's menu.

### Edit a menu item

- **Actor:** Owner
- **Preconditions:** Logged in as an owner, and the item is on the owner's truck menu.
- **Main flow:** Owner selects an item, changes its name, price, or description, and the system validates and saves the change.
- **Alternate/error flows:** Same validation errors as "Add a menu item".
- **Postconditions:** The updated item is shown to all users.

### Remove a menu item

- **Actor:** Owner
- **Preconditions:** Logged in as an owner, and the item is on the owner's truck menu.
- **Main flow:** Owner selects an item, chooses "Remove", and confirms. The system deletes it.
- **Alternate/error flows:** The owner cancels the confirmation, so nothing changes.
- **Postconditions:** The item no longer appears on the menu.

### Reply to a review

- **Actor:** Owner
- **Preconditions:** Logged in as an owner. The review is for the owner's truck and has no reply yet.
- **Main flow:**
    1. Owner opens a review on their truck and chooses "Reply".
    2. Owner enters the reply text.
    3. System validates the text and saves the reply, linked to the review and shown beneath it.
- **Alternate/error flows:**
    - The text is empty or too long, so the system shows an error.
    - The review is for another owner's truck, so the system rejects the reply.
    - The review already has a reply, so the system rejects the reply.
- **Postconditions:** The review has one owner reply visible to all users.

### Image Storage

### Aggregate rating calculation

### Review Summary Generation

## Stretch Use Cases

### Edit a review

- **Actor:** Reviewer
- **Preconditions:** Logged in as a reviewer who authored the review.
- **Main flow:**
    1. Reviewer opens their own review and chooses "Edit".
    2. Reviewer changes the ratings, text, or images.
    3. System validates using the same rules as "Post a review" and "Attach images to a review", saves the changes, and recalculates the truck's aggregate ratings.
- **Alternate/error flows:** Validation errors as in "Post a review". The reviewer is not the author, so the action is not offered and is rejected if attempted.
- **Postconditions:** The review is updated and marked as edited.

### Delete a review

- **Actor:** Reviewer
- **Preconditions:** Logged in as a reviewer who authored the review.
- **Main flow:**
    1. Reviewer chooses "Delete" on their own review and confirms.
    2. System deletes the review, its images, and any owner reply, then recalculates the truck's aggregate ratings.
- **Alternate/error flows:** The reviewer cancels the confirmation, so nothing changes.
- **Postconditions:** The review no longer exists and the reviewer may post a new one for this truck.

### Edit a reply to a review

- **Actor:** Owner
- **Preconditions:** Logged in as an owner who wrote the reply.
- **Main flow:** Owner chooses "Edit" on their reply, changes the text, and the system validates and saves it.
- **Alternate/error flows:** Same validation errors as "Reply to a review".
- **Postconditions:** The reply text is updated and marked as edited.

### Delete a reply to a review

- **Actor:** Owner
- **Preconditions:** Logged in as an owner who wrote the reply.
- **Main flow:** Owner chooses "Delete" on their reply and confirms. The system removes the reply.
- **Alternate/error flows:** The owner cancels the confirmation, so nothing changes.
- **Postconditions:** The review has no reply, and the owner may post a new one.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |

---

## Entities

- Category (enum)
    - `OVERALL`, `SERVICE`, `FOOD_QUALITY`, `VALUE_FOR_MONEY`
- UserType (enum)
    - `REVIEWER`, `OWNER`
- PriceRange
    - start: BigDecimal (2 decimal places)
    - end: BigDecimal (2 decimal places)
- Image
    - path: String
- User
    - type: UserType
    - username: String (unique)
    - *note: there is a one-to-one relationship between FoodTruck and User (with type `OWNER` or `null`), but this will be encoded in the database*
- FoodTruck
    - name: String
    - location: Location
    - cuisineType: List\<String\>
    - priceRange: PriceRange
    - photos: List\<Image\>
    - menu: List\<MenuItem\>
    - reviews: List\<Review\>
- Location
    - latitude: double within [-90, 90]
    - longitude: double within [-180, 180]
- MenuItem
    - name: String
    - description: String 
    - price: BigDecimal (2 decimal places)
- Review
    - author: User (type `REVIEWER`; `null` if imported from Google Maps)
    - authorDisplayName: String (used for imported reviews)
    - source: enum (`APP`, `GOOGLE_MAPS`)
    - ratings: Map\<Category, Integer\> (each value in [1, 5], one entry per category)
    - text: String (optional)
    - photos: List\<Image\> (optional, may be empty)
    - reply: Reply (optional, at most one)
    - timestamp: LocalDateTime (from `java.time` package)
- Reply
    - text: String
    - timestamp: LocalDateTime (from `java.time` package)
    - *note: the author is always the owner of the review’s truck, so no author field is needed*

---

## API Usage

1. Custom API
   - food truck location data
2. Gemini API 
   - food truck review summaries
   - [docs](https://aistudio.google.com/docs)
3. Google Maps API
   - prepopulate reviews using Google's review data
   - note: only a maximum 5 reviews can be returned
   - [docs](https://developers.google.com/maps/documentation/places/web-service/reference/rest/v1/places)
---

## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

[Any additional project information.]
