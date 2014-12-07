//
//  CustomCell.h
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomCell : UITableViewCell
{
    IBOutlet UILabel *name;
    IBOutlet UILabel *quantity;
}

@property (nonatomic, strong) UILabel *name;
@property (nonatomic, strong) UILabel *quantity;
@property (nonatomic, strong) NSString *nameString;
@property NSInteger *quantityInt;

-(void)createCell:(NSString*)item;

@end
