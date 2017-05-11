def inverse(ls) :
    ls_tem = []
    for i in range(len(ls)-1,-1,-1):
       ls_tem.append(ls[i])
    return ls_tem


def all_inverse(ls):
    ls = inverse(ls)
    for item in ls :
        if type(item) == list and len(item)>1:
            print "use"
            ls[ls.index(item)] = all_inverse(item)
            print item
    return ls

b=[1, [12, 3, [4, [5, 16]]]]
print all_inverse(b)



