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
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onAdd:(id)sender
{
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
        } else {
            NSLog(@"Error Saving");
        }
    }];
    
}

@end