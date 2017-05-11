

# First, I removed the split... it is already an array
str1 = 'ab cdefg sh'
str2 = 'abcdefg b sh'


#then creating a new variable to store the result after  
#comparing the strings. You note that I added result2 because 
#if string 2 is longer than string 1 then you have extra characters 
#in result 2, if string 1 is  longer then the result you want to take 
#a look at is result 2

result1 = ''
result2 = ''

#handle the case where one string is longer than the other
maxlen=len(str2) if len(str1)<len(str2) else len(str1)

#loop through the characters
for i in range(maxlen):
    #use a slice rather than index in case one string longer than other
    letter1=str1[i:i+1]
    letter2=str2[i:i+1]
    #create string with differences
    if letter1 != letter2:
        result1+=letter1
        result2+=letter2

#print out result
print ("Letters different in string 1:",result1)
print ("Letters different in string 2:",result2)