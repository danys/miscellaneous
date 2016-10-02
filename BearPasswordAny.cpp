#include <string>
#include <vector>

using namespace std;

class BearPasswordAny{
    public:
		string findPassword(vector<int> v);
};

int nSubstringsSizeN(int n, string &v){
	int res=0,cc=0;
    char c=' ';
    for(int i=0;i<v.size();i++){
    	cc=0;
        for(int j=i;j<v.size();j++){
        	if (j==i){
                c=v[i];
                if (c=='x') break;
                cc=1;
            }
            else if (c==v[j]){
            	cc++;
            }
            else if (c!=v[j]){
            	break;
            }
            if (cc==n){
            	res++;
                break;
            }
        }
    }
    return res;
}

char flip(char c){
	if (c=='a') return 'b';
    else return 'a';
}

bool putStr(string &str, int len){
	int i=0;
    char c=' ';
    while((i<str.size()) && ((str[i]=='a') || (str[i]=='b'))) {c=str[i];i++;}
    if ((i==str.size()) || (i+len>str.size())) return false;
    for(int j=1;j<=len;j++) {str[i]=flip(c);i++;}
    return true;
}

string BearPasswordAny::findPassword(vector<int> v){
	string s;
    for(int i=0;i<v.size();i++) s.push_back('x');
    int count,nPut;
    for(int i=v.size()-1;i>=0;i--){
    	count = nSubstringsSizeN(i+1,s);
        if (count>v[i]) {s.clear();return s;}
        nPut = v[i]-count;
        for(int z=1;z<=nPut;z++) {
            if (!putStr(s,i+1)) {s.clear();return s;}
        }
    }
    for(int i=0;i<s.size();i++) if (s[i]=='x') {s.clear();return s;}
    return s;
}
