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

Google Maps for UofT food spots on campus

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
10. import reviews from Google Maps API

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

### Login
- PAs: user or owner
- Trigger: submit button on login form
- users or owners log in depending on the account
- input data
  - username, password
- result
  - view model updated with user

### Logout

### Sign up

### Get food trucks
- PAs: user or owner
- Trigger:
- get each food truck's location and display them as pins on the map

### Show food truck details
- show food truck details (once a pin is clicked)

### View a food truck's reviews
- also allow them to be sorted by a rating category

### Generate summary of reviews
- generate LLM summary of food truck reviews
- only if there are a sufficient number of reviews
- only generate a new summary if there are x new reviews since previous summary was generated (prevent too many API calls)

### Write review
- user can write a review with fields specified in [entities](#entities)

### Reply to review (owner)
- an owner can reply to a review and it should be indicated in the UI that they are the owner

### Update food truck details (owner)
- a food truck owner can update any field defined in FoodTruck in [entities](#entities)

(Add additional use cases as needed.)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |

---

## Entities

- Rating
   - category: String (overall, service, food quality, value for money)
   - num: int (1-5)
- Review
   - ratings: List\<Rating\> (list of length 4, each element being one of the categories above)
   - text: String (optional)
   - photos: image (optional)
- FoodTruck
  - owner: User (with type owner, or null if no owner associated with it)
  - location: Location
  - reviews: List\<Review\>
  - cuisineType: List\<String\>
  - menu: Menu
- Menu
  - List\<MenuItem\>
- MenuItem
  - description: String
  - price: float (2 decimals)
- Location
  - latitude/longitude coordinates
- User
   - type: int (a constant representing owner or normal user)
   - username: String
   - password: String

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
