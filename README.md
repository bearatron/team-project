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

---

## Use Cases

useful constants

| Constant | Value |
|---|---|
| `MAX_REVIEW_TEXT_LENGTH` | 1000 characters |
| `MAX_REPLY_TEXT_LENGTH` | 500 characters |
| `MAX_IMAGES_PER_REVIEW` | 5 |
| `MAX_IMAGE_SIZE` | 5 MB |
| `ALLOWED_IMAGE_TYPES` | JPEG, PNG |
| `MIN_PASSWORD_LENGTH` | 8 |
| `MIN_REVIEWS_FOR_SUMMARY` | 5 text reviews |
| `SUMMARY_REGEN_THRESHOLD` | 5 new text reviews |
| `GOOGLE_IMPORT_MAX` | 5 (API limit) |

### Sign up as a reviewer

- **Actor:** User (not logged in)
- **Preconditions:** None
- **Main flow:**
    1. User submits a username, a password and a repeated password.
    2. System validates the input.
    3. System creates a reviewer account and records the user as the current logged-in user.
    4. System returns the username and user type.
- **Alternate/error flows:**
    - The username is already taken (by a reviewer or an owner account).
    - The passwords do not match, a field is empty, or the password is shorter than `MIN_PASSWORD_LENGTH`.
- **Postconditions:** A reviewer account exists and the user is logged in.

### Sign up as an owner and register a truck

- **Actor:** User (not logged in)
- **Preconditions:** None
- **Main flow:**
    1. User submits credentials (as in "Sign up as a reviewer") and truck details: name, cuisine list, price range and initial location.
    2. System validates the credentials and truck details.
    3. System creates the owner account and the truck at the same time (either both succeed or both fail) and records the user as the current logged-in user.
    4. System returns the username, user type and truck name.
- **Alternate/error flows:**
    - Same credential errors as "Sign up as a reviewer".
    - The truck name is empty, the cuisine list is empty, or the price range is invalid (start greater than end, or negative).
    - The initial location is not a valid coordinate.
- **Postconditions:** An owner account is linked to exactly one new truck, and the user is logged in.

### Log in

- **Actor:** Reviewer or owner
- **Preconditions:** The user has an account and is not logged in.
- **Main flow:**
    1. User submits a username and password.
    2. System verifies the credentials and records the user as the current logged-in user.
    3. System returns the username and user type.
- **Alternate/error flows:** Unknown username or wrong password gives the same generic error message, so it does not reveal which was wrong.
- **Postconditions:** The user is logged in and their account type is known.

### Log out

- **Actor:** Reviewer or owner
- **Preconditions:** The user is logged in.
- **Main flow:**
    1. User requests to log out.
    2. System clears the current logged-in user and returns a confirmation.
- **Postconditions:** No user is logged in. Reviewer and owner use cases are rejected until the next login.

### View trucks on the campus map

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User requests the trucks.
    2. System loads all trucks.
    3. System returns each truck's identifier, name and current location.
- **Alternate/error flows:**
    - There are no trucks, so the system returns an empty list.
    - The trucks cannot be loaded, so the system returns an error.
- **Postconditions:** None (read-only).

### View a truck's details

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User submits a truck identifier.
    2. System loads the truck.
    3. System computes the truck's average rating and rating count for each category (see [Aggregate ratings](#aggregate-ratings)).
    4. System returns the name, location, cuisine, price range, the owner's photos, the menu and the averages.
- **Alternate/error flows:**
    - The truck does not exist, so the system returns an error.
    - The truck has no photos, no menu or no reviews, so the system returns those parts empty (no averages when there are no ratings).
- **Postconditions:** None (read-only).
- **Note:** Reviews are retrieved through "View a truck's reviews".

### Post a review

- **Actor:** Reviewer
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits a truck identifier, a rating from 1 to 5 for each rating category, and optional text (images are handled in [this use case](#attach-images-to-a-review)).
    2. System validates the input.
    3. System saves the review with the reviewer, truck, source `APP` and the current timestamp.
    4. System returns the new review.
- **Alternate/error flows:**
    - The user is not a reviewer, or the truck does not exist.
    - A rating category is missing or outside 1-5.
    - The text exceeds `MAX_REVIEW_TEXT_LENGTH`.
    - The reviewer has already reviewed this truck.
- **Postconditions:** The review is saved on the truck, so the truck's averages include it the next time they are read.

### Attach images to a review

- **Actor:** Reviewer
- **Preconditions:** A user is logged in.
- **Main flow:** This is part of the "Post a review" interactor (same request, same submit), not a separate request.
    1. User includes one or more image files in the review.
    2. System validates each file's type and size and the total image count.
    3. When the review is saved, the system stores the images through [image storage](#image-storage) and links them to the review.
- **Alternate/error flows:**
    - The file type is unsupported, the file exceeds `MAX_IMAGE_SIZE`, or the image count exceeds `MAX_IMAGES_PER_REVIEW`, so the system rejects that file and keeps the rest.
    - The file cannot be read or stored, so the system skips it, reports it in the output, and the review is still saved.
- **Postconditions:** The review has zero or more linked images.

### View a truck's reviews

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User submits a truck identifier.
    2. System loads the truck's reviews, newest first.
    3. System returns each review's author display name, source, category ratings, text, images, timestamp and owner reply (if any).
- **Alternate/error flows:**
    - The truck does not exist, so the system returns an error.
    - The truck has no reviews, so the system returns an empty list.
- **Postconditions:** None (read-only).

### Sort reviews by rating category

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User submits a truck identifier, a rating category and a direction (highest or lowest first).
    2. System loads the truck's reviews and orders them by that category.
    3. System returns the ordered reviews.
- **Alternate/error flows:**
    - The truck does not exist, so the system returns an error.
    - Reviews without a rating in that category (e.g. imported reviews) are placed last.
    - Ties are broken by newest first.
    - The truck has no reviews, so the system returns an empty list.
- **Postconditions:** None (read-only).

### Generate a review summary

- **Actor:** User
- **Preconditions:** None
- **Main flow:**
    1. User submits a truck identifier.
    2. System loads the truck's stored summary and the number of text reviews when it was generated.
    3. If the truck has fewer than `MIN_REVIEWS_FOR_SUMMARY` text reviews, system returns a "too few reviews" result.
    4. If no summary exists, or at least `SUMMARY_REGEN_THRESHOLD` new text reviews exist since the stored count, system sends the review text to the [summary generator](#review-summary-generator) and stores the new summary with the current count.
    5. System returns the summary.
- **Alternate/error flows:**
    - The truck does not exist, so the system returns an error.
    - The summary generator fails or times out, so the system returns an error, keeps any existing summary, and the rest of the application is unaffected.
- **Postconditions:** If a new summary was generated, it and the review count are stored, otherwise nothing changes.

### Update truck location

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits a new latitude and longitude.
    2. System finds the owner's truck.
    3. System validates the coordinates.
    4. System saves the new location and returns it.
- **Alternate/error flows:**
    - The user is not an owner or has no truck.
- **Postconditions:** The truck's location is updated for all users.

### Edit cuisine and price range

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits a cuisine list and/or a price range.
    2. System finds the owner's truck and validates the input.
    3. System saves the change and returns the updated details.
- **Alternate/error flows:**
    - The user is not an owner or has no truck.
    - The cuisine list is empty, or the price range is invalid (start greater than end, or negative).
- **Postconditions:** The truck details are updated.
- **Note:** A food truck's name cannot be edited by its owner 

### Import Google Maps reviews

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits the identifier of the truck's Google Maps listing.
    2. System finds the owner's truck.
    3. System fetches up to `GOOGLE_IMPORT_MAX` reviews for the listing through the review import gateway.
    4. System saves the reviews not already imported to the truck, with source `GOOGLE_MAPS` and their original author display name, rating, text and date.
    5. System returns the number of reviews imported.
- **Alternate/error flows:**
    - The user is not an owner or has no truck.
    - The listing is not found, so the system returns an error.
    - The external service fails or is rate-limited, so nothing is imported.
    - Reviews were already imported, so the system skips duplicates.
- **Postconditions:** The truck has imported reviews, distinguishable from native reviews by their source.
- **Note**: Reviews only are imported to the `OVERALL` category and the others are left blank. Imported reviews will be included in the computation of the `OVERALL` rating average.

### Add a menu item

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits an item name, a price and an optional description.
    2. System finds the owner's truck and validates the input.
    3. System adds the item to the truck's menu and returns it.
- **Alternate/error flows:**
    - The user is not an owner or has no truck.
    - The name is empty, the price is non-numeric, zero or negative, or the name duplicates an existing item.
- **Postconditions:** The item is on the truck's menu.

### Edit a menu item

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits an item identifier and the new name, price and/or description.
    2. System finds the owner's truck and the item on its menu, and validates the input.
    3. System saves the change and returns the updated item.
- **Alternate/error flows:**
    - The user is not an owner or has no truck, or the item is not on the owner's menu.
    - Same validation errors as "Add a menu item" (the duplicate-name check ignores the item being edited).
- **Postconditions:** The updated item is shown to all users.

### Remove a menu item

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits an item identifier.
    2. System finds the owner's truck and the item on its menu, and removes it.
    3. System returns a confirmation.
- **Alternate/error flows:** The user is not an owner or has no truck, or the item is not on the owner's menu.
- **Postconditions:** The item is no longer on the menu.

### Reply to a review

- **Actor:** Owner
- **Preconditions:** A user is logged in.
- **Main flow:**
    1. User submits a review identifier and the reply text.
    2. System validates the text and finds the review.
    3. System saves the reply on the review with the current timestamp and returns it.
- **Alternate/error flows:**
    - The user is not an owner, or the review does not exist.
    - The text is empty or longer than `MAX_REPLY_TEXT_LENGTH`.
    - The review is for another owner's truck.
    - The review already has a reply.
- **Postconditions:** The review has one owner reply visible to all users.
- **Note:** Owners can also reply to imported Google Maps reviews.

## Data Access Interfaces

Data access interfaces that allow the use cases to access external services


### Image storage

- **Used by:** Attach images to a review, Upload a truck photo, Remove a truck photo, Delete a review
- **Save:** Given an image file that has already passed validation by the calling use case, store it exactly once under a unique ID and return its path. If the write fails, remove any partial file, report a storage error and leave nothing behind.
- **Delete:** Given an image path, delete the stored file. If deletion fails, report the failure so the caller can still remove the `Image` record and queue the file for later cleanup.
- **Open question:** Should thumbnails be generated?
- Resolving paths for display, caching and placeholder images for missing files are view and framework concerns, not part of this interface.

### Review summary generator

- **Used by:** Generate a review summary
- **Contract:** Given a list of review texts, return a summary string, or report failure or timeout. Uses the Gemini API.

### Google Maps review import

- **Used by:** Import Google Maps reviews
- **Contract:** Given a Google Maps listing identifier, return up to `GOOGLE_IMPORT_MAX` reviews (author display name, rating, text, date), or report that the listing was not found or the service failed. Uses the Google Maps API.

### Aggregate ratings

- For each category, return the average of the ratings that are present (rounded to two decimal places), plus the number of ratings used.
- A truck with no ratings in a category has no average for it (shown as "No ratings yet" by the view), which avoids division by zero.
- Reviews missing a category are excluded from that category's average.
- **Note:** Imported reviews only count toward the average calculation for the `OVERALL` category.

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
        - *Note: The number of mandatory categories depends on source. `APP` requires all 4 categories to be present, while `GOOGLE_MAPS` only requires `OVERALL`*
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
