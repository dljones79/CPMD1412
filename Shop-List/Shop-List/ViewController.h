//
//  ViewController.h
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import <ParseUI/ParseUI.h>

@interface ViewController : UIViewController <PFLogInViewControllerDelegate, PFSignUpViewControllerDelegate>
{
    IBOutlet UIButton *viewButton;
    IBOutlet UIButton *addButton;
    IBOutlet UIButton *logButton;
}

-(IBAction)onLog:(id)sender;

@end

