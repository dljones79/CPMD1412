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
    /*
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *newCell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (newCell == nil){
        newCell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    PFObject *item = [itemArray objectAtIndex:indexPath.row];
    [newCell.textLabel setText:[item objectForKey:@"item"]];
    */
    
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

-(void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath
{
    PFObject *pObj = [itemArray objectAtIndex:indexPath.row];
    [pObj removeObjectForKey:@"item"];
    [pObj saveInBackground];
    
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

-(IBAction)onBack:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
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
