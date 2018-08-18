# AndroidArchitectureComponents
Sample application to demonstrate Android Architecture Components

# Why Acchitecture is important ??
- We want to produce 
  1. Maintainable: Maintainable software is a piece of software that has the ability
            
            - to fix a bug without introducing a new one,
            - to fix a bug without it reoccurring again and again,
            - to fix a bug with editing a low number of components.
            
  2. Extendable: Extendable software is a piece of software that has the ability
  
            - to add a new feature with a minimum change of current components  
            - to add a new feature without changing the shape of the original architecture.
            
  3. Testable: Testable software is a piece of software that has the ability 
  
            - to test each component separately that costs low-maintenance effort in order to improve or update our testing code.

# Android Architecture Components
- A new collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence.
- In Short these set of libraries will help us address common issues of 

          - configuration change
          - memory leaks
          - writing testable apps 
          - reduce the boiler plate code while maintaining the architecture.
      
   # 1. Lifecycle: 
   With the help of this component we can build lifecycle-aware components. These components automatically adjust their behavior based on the current lifecycle of an activity or fragment.
   
      Advantages :
        - Decouple LC logic per component.
        - Makes activity LC methods to be shorter
      
   # 2. ViewModel:
   It is the component designed to store and manage UI-related data so that the data survives configuration changes such as screen rotations. It should never access view hierarchy or hold a reference back to the Activity or the Fragment.
      
      Summary:
        - It’s a lifecycle aware component
        - Created only once per lifecycle owner (Our MainActivity) 
        - Contains our data access logic and other application related logic

      Advantages:
        - Survives configuration changes 
        - Efficient in terms of memory 
        - Sharable between components

      
   # 3. LiveData:
   LiveData is a component which holds the value (data holder) and allows to observe for changes in the value. It is designed to hold the data of ViewHolder but it can also be used in sharing data in your application. It is lifecycle aware that means it is aware if the Activity is alive to receive the data or not preventing crashes.
      
      Summary:
        - It’s a lifecycle aware component 
        - Get data updates via observers 
        - How to update LiveData objects from different threads 
        - How to share data between multiple components 
        
      Advantages:
        - It’s a lifecycle aware component 
        - Decouples data access logic from the one who consumes it  
        - Bound to a life cycle owner

      
   # 4. Room:
   Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
    
      Advantages:
        - Compile-time verification of raw SQL queries.
        - Eliminates the boilerplate code to convert between SQL queries and Java data objects.
        - No need of writing Cursor or Loader
