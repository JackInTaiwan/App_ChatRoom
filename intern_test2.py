input = int(raw_input("please enter an number"))

ls = range(1,input+1)

for i in range(1,(input/3)+1):
    if (3*i)%15!=0:

        ls.remove(3*i)
for i in range(1,(input/5)+1):
    if (5*i)%15!=0:
        if 5*i in ls:
            ls.remove(5*i)

print len(ls)

