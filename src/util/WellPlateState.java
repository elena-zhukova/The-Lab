//-----------------------------------------
// 
// ENUM			: WellPlateState.java
//
// REMARKS		: declaring fsm for checking which wells of well plate are full and which reactions happened
//
//-----------------------------------------
package util;

public enum WellPlateState 
{
	EMPTY, AFILLED, BFILLED, FULL,
	ACHECKED, BCHECKED, ALLCHECKED;
}
