//
//  ViewController.m
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import "ViewController.h"
#import "ListViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    // Test network
    if (![self isConnected]){
        // not network connection
        NSLog(@"No Network Connection.");
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Network Error." message:@"You must connect to a network to access full application features." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    } else {
        // network is connected
        if ([PFUser currentUser]){
            
        } else {
            PFLogInViewController *login = [[PFLogInViewController alloc] init];
            login.delegate = self;
            login.signUpController.delegate = self;
            [self presentViewController:login animated:YES completion:nil];
        }
    }
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)logInViewController:(PFLogInViewController *)logInController didLogInUser:(PFUser *)user{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)logInViewControllerDidCancelLogIn:(PFLogInViewController *)logInController{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)signUpViewController:(PFSignUpViewController *)signUpController didSignUpUser:(PFUser *)user{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)signUpViewController:(PFSignUpViewController *)signUpController didFailToSignUpWithError:(NSError *)error{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(IBAction)onLog:(id)sender{
    if ([PFUser currentUser]){
        [PFUser logOut];
        PFLogInViewController *login = [[PFLogInViewController alloc] init];
        login.delegate = self;
        login.signUpController.delegate = self;
        [self presentViewController:login animated:YES completion:nil];
    }
}

-(BOOL)isConnected{
    Reachability *networkReachable = [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [networkReachable currentReachabilityStatus];
    return netStatus != NotReachable;
}

@end
