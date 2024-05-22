# Job Finder Application

This is a **simple job finder Spring Boot application**, containing **three different tasks**:

- *Companies* can order *jobs*
- Each *job* contains one or more *shifts*
- *Talents* (workers) can be booked for *shifts*

### Product boundary conditions

- A *job* should have at least one *shift*
- The start date of a *job* cannot be in the past
- The end date of a *job* should be after the start date
- A *shift*'s length should equal to 8 hours

### Out of scope

In order to keep the scope reasonably sized, there is no possibility for a company to request jobs for specific times.

## Objective

Your job is to modify the existing service so it satisfies the following requirements:

### Task A

- **AS** a *company*
- **I CAN** cancel a *job* I ordered previously
- **AND** if the *job* gets cancelled all of its *shifts* get cancelled as well

### Task B

- **AS** a *company*
- **I CAN** cancel a single *shift* of a job I ordered previously

### Task C

- **AS** a *company*
- **I CAN** cancel all of my shifts which were booked for a specific talent
- **AND** replacement shifts are created with the same dates
