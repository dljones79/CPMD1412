//
//  ListViewController.m
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import "ListViewController.h"

@interface ListViewController ()

@end

@implementation ListViewController

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    
}

- (void)viewWillAppear:(BOOL)animated
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    // Test network
    if (![self isConnected]){
        // no connection
    } else {
        // connection
        // Create a query
        PFQuery *itemQuery = [PFQuery queryWithClassName:@"Item"];
        [itemQuery whereKeyExists:@"item" ];
        
        // Run query
        [itemQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error){
                NSLog(@"Query Good.");
                NSLog(@"Array Size: %lu", (unsigned long)objects.count);
                itemArray = objects;
                [itemTable reloadData];
            } else {
                NSLog(@"Query Bad.");
            }
        }];
        
        // Timer for polling server
        [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(timerFired:) userInfo:nil repeats:YES];
    }

    // Add a swipe gesture recognizer
    UISwipeGestureRecognizer *swipeRecognizer = [[UISwipeGestureRecognizer alloc]initWithTarget:self action:@selector(handleSwipeRight:)];
    [swipeRecognizer setDirection:(UISwipeGestureRecognizerDirectionRight)];
    [itemTable addGestureRecognizer:swipeRecognizer];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return itemArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    PFObject *pObj = [itemArray objectAtIndex:indexPath.row];
    itemName = [pObj objectForKey:@"item"];
    qty = [pObj objectForKey:@"quantity"];
    quantity = [qty stringValue];
    //NSLog(@"Item Name: %@",itemName);
    
    CustomCell *newCell = [tableView dequeueReusableCellWithIdentifier:@"customCell"];
    newCell.name.text = itemName;
    newCell.quantity.text = quantity;
    
    return newCell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    pObject = [itemArray objectAtIndex:indexPath.row];
    objId = pObject.objectId;
    
    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Edit" message:@"Enter a new quantity:" delegate:self cancelButtonTitle:@"Save" otherButtonTitles:nil, nil];
    alert.alertViewStyle = UIAlertViewStylePlainTextInput;
    [alert show];
}

-(IBAction)onBack:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(void)handleSwipeRight:(UISwipeGestureRecognizer *)gestureRecognizer{
    CGPoint loc = [gestureRecognizer locationInView:itemTable];
    NSIndexPath *indexPath = [itemTable indexPathForRowAtPoint:loc];
    
    PFObject *pObj = [itemArray objectAtIndex:indexPath.row];
    [pObj deleteInBackground];
    
    // Create a query
    PFQuery *itemQuery = [PFQuery queryWithClassName:@"Item"];
    [itemQuery whereKeyExists:@"item" ];
    
    // Run query
    [itemQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error){
            NSLog(@"Query Good.");
            NSLog(@"Array Size: %lu", (unsigned long)objects.count);
            itemArray = objects;
            [itemTable reloadData];
        } else {
            NSLog(@"Query Bad.");
        }
    }];
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    NSLog(@"%@", [alertView textFieldAtIndex:0].text);
    
    // Number validation via regular expression
    NSString *regEx = @"^([1-9][0-9]{0,2})?(\\.[0-9]?)?$";
    NSPredicate *numPred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regEx];
    
    NSString *updatedQuantity = [alertView textFieldAtIndex:0].text;
    
    if (![numPred evaluateWithObject:updatedQuantity] || [updatedQuantity isEqualToString:@""]){
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Invalid Input" message:@"You must enter a valid quantity." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    } else {
        NSNumberFormatter *formatter = [[NSNumberFormatter alloc]init];
        [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
        NSNumber *number = [formatter numberFromString:updatedQuantity];
        
        PFQuery *parseQuery = [PFQuery queryWithClassName:@"Item"];
        [parseQuery getObjectInBackgroundWithId:objId block:^(PFObject *object, NSError *error) {
            object[@"quantity"] = number;
            [object saveInBackground];
        }];
        
        // Create a query
        PFQuery *itemQuery = [PFQuery queryWithClassName:@"Item"];
        [itemQuery whereKeyExists:@"item" ];
        
        // Run query
        [itemQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error){
                NSLog(@"Query Good.");
                NSLog(@"Array Size: %lu", (unsigned long)objects.count);
                itemArray = objects;
                [itemTable reloadData];
            } else {
                NSLog(@"Query Bad.");
            }
        }];
        
        [itemTable reloadData];
    }

}

-(void)timerFired:(NSTimer*)timer {
    // Create a query
    PFQuery *itemQuery = [PFQuery queryWithClassName:@"Item"];
    [itemQuery whereKeyExists:@"item" ];
    
    // Run query
    [itemQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error){
            NSLog(@"Query Good.");
            NSLog(@"Array Size: %lu", (unsigned long)objects.count);
            itemArray = objects;
            [itemTable reloadData];
        } else {
            NSLog(@"Query Bad.");
        }
    }];
}

-(BOOL)isConnected{
    Reachability *networkReachable = [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [networkReachable currentReachabilityStatus];
    return netStatus != NotReachable;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
