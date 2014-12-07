//
//  CustomCell.m
//  Shop-List
//
//  Created by David Jones on 12/6/14.
//  Copyright (c) 2014 David Jones. All rights reserved.
//

#import "CustomCell.h"

@implementation CustomCell
@synthesize name, quantity, nameString, quantityInt;

- (id) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self){
        // Initialization code
    }
    return self;
}

-(void)createCell:(NSString *)item
{
    self.name.text = item;
}

-(void)awakeFromNib
{
    // Initialization code
}

-(void)setSelected:(BOOL)selected andimated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    
    // configure the view for the selected state
}

@end
