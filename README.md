# Neurable - Android Technical Challenge
by Evan Liu

## Introduction
Welcome! This is my submission for the technical challenge.

As stated by the assignment, the goal of this project was to demonstrate understanding in Jetpack Compose, Kotlin Coroutines and BLE implementation.
The app uses just one Activity and other tools I used include ViewModel and NavHost to navigate between the screens.

Here I'll briefly run through the flow of the app.

## Part 1: Home
The HomeScreen is a simple Composable using a Column, a Row and two Buttons. Here is where the app

![NeurableBle HomeScrreen cropped](https://github.com/user-attachments/assets/de11d8b8-c50c-4151-9192-a21af0696056)

The left button navigates the Focus Scoring screen and the right button navigates to the BLE portion of the project.
The navigation is done via NavHost as mentioned earlier as I felt this was the most appropriate way to navigate, given that utilizing Compose was one of the main points.

## Part 2: FocusScreen
Clicking on the left button calls the NavHostController to navigate() to this screen using a defined route.

![NeurableBle FocusScreen cropped](https://github.com/user-attachments/assets/c6046335-6b66-448c-8f75-a94df2f2edb1)

This screen uses a single button that toggles between two states to start and stop receiving a focus score.

![NeurableBle FocusScreen active cropped](https://github.com/user-attachments/assets/ef78b1e7-4fde-4ce2-a00a-0dcd9cc924ce)

Using the ViewModel's viewModelScope, it launches a coroutine that calls a method to fetch the focus score every 5 seconds before yielding for a cancel call, continuing if not canceled.
Two variables are remembered to toggle the button's appearance between the green start button and red stop button: the button text and the button color.

To leave this screen, BackHandler has been added to have the NavHostController navigate back to the HomeScreen. From there, we can navigate to the BLEScreen for the mock BLE implementation.

## Part 3: BLEScreen
![image](https://github.com/user-attachments/assets/7011c201-fee3-4f0c-9b77-8ab277be2227)

Here is the BLEScreen and when first navigating to it, the user will be prompted to grant permissions.
These permissions differ from before and after API level 31. Up until API 30 the permissions to request were BLUETOOTH, BLUETOOTH_ADMIN, and after 31 they are BLUETOOTH_SCAN and BLUETOOTH_CONNECT.
When trying to scan or connect, the API level is used to decide which permissions should be checked.

Though there are multiple libraries available, such as Kable and NordicSemiconductor, I opted to go with the built-in API Android has for BLE as I have not had professional experience
implementing BLE and found this one the most straightforward and well documented. 

In my implementation, I use a ViewModel to perform the scanning for devices and add them to a MutableLiveData list of ScanResults.
A ScanCallback returns ScanResults which are compared against a list of previously seen device addresses.
If this device address is new, it is added to the list of ScanResults and the address is added to the list of seen devices.

![image](https://github.com/user-attachments/assets/fc1c52b9-f4d8-40e0-9f55-1144476ed8c6)

This list is then observed in the Composable screen and Buttons are added to a LazyColumn for each unique device detected.
When clicked these buttons will attempt to connect to the device. 
Unfortunately I lack any devices to test this functionality with and can only write a mock implementation that ends at scanning for devices.

BLE was a very interesting subject to learn more about, considering its prevalence in people's everyday life, and I would be very interested in pursuing this further.

I hope my app demonstrates the desired technologies.
