//
//  AddController.m
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import "AddController.h"

@interface AddController ()

@end

@implementation AddController


-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    if (![self isConnected]){
        // no connection
        addButton.enabled = false;
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Network Error." message:@"You must connect to a network to add items to your list." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    } else {
        addButton.enabled = true;
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onAdd:(id)sender
{
    // Number validation via regular expression
    NSString *regEx = @"^([1-9][0-9]{0,2})?(\\.[0-9]?)?$";
    NSPredicate *numPred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regEx];
    
    if ([itemName.text isEqual: @""] || [quantity.text isEqual: @""]){
        NSLog(@"Fields Empty.");
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Invalid Input" message:@"You must enter an item and quantity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    } else if (![numPred evaluateWithObject:quantity.text]){
        NSLog(@"Not a number.");
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Invalid Input" message:@"You must enter a valid quantity." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    } else {
        NSNumberFormatter *formatter = [[NSNumberFormatter alloc]init];
        [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
        NSNumber *number = [formatter numberFromString:quantity.text];
        
        PFObject *newItem = [PFObject objectWithClassName:@"Item"];
        newItem[@"item"] = itemName.text;
        newItem[@"quantity"] = number;
        
        PFACL *acl = [PFACL ACLWithUser:[PFUser currentUser]];
        [acl setPublicReadAccess:NO];
        [newItem setACL:acl];
        
        [newItem saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (!error){
                NSLog(@"Saved");
                itemName.text = @"";
                quantity.text = @"";
                
                UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Item Saved" message:@"Your item has been saved." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                [alert show];
            } else {
                NSLog(@"Error Saving");
            }
        }];
    }
}

-(IBAction)onBack:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(BOOL)isConnected{
    Reachability *networkReachable = [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [networkReachable currentReachabilityStatus];
    return netStatus != NotReachable;
}

@end