package model;

public enum Priority {
    LOW, MEDIUM, HIGH;
    public static Priority fromChoice(int choice){
        if(choice == 1){
            return LOW;
        }else if(choice == 3){
            return HIGH;
        }else{
            return MEDIUM;
        }
    }
}
