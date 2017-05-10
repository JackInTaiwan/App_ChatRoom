__author__ = 'jack'
a=[1,2,3,5,4,2]
b=[i for i in a]
b[0]=2
c=a[3::]
c[1]=100
print sorted(a[2::])
#c=a.pop()
#a.pop()
print a,b,c
