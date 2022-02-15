#include <iostream>
#include <vector>

using namespace std;

class Pet
{
    protected:
        string name;
        int code;
    
    public:
        Pet(){}
        virtual ~Pet() {}
        virtual void make_noise()
        {
            cout << "rrrrrrrr" << endl;
        }

        string get_name()
        {
            return this->name;
        }

        bool operator==(Pet other)
        {
            if (other.name!=this->name) return false;
            else return true;
        }
    
};

class Dog : public Pet
{
    public:
        Dog(){}

        Dog(string name, int code) 
        {
            this->name=name;
            this->code=code;
        }

        void make_noise()
        {
            cout << "gau gau" << endl;
        }

};

class Cat : public Pet
{
    public:
        Cat(){}

        Cat(string name, int code) 
        {
            this->name=name;
            this->code=code;
        }

         void make_noise()
        {
            cout << "meo meo" << endl;
        }

};

class Address 
{
    protected:
        string street;
        int number;
        string city;

    public:
        Address(){}

        Address(string street, int number, string city)
        {
            this->street = street;
            this->number = number;
            this->city = city;
        }

        void print_info()
        {
            cout << this->street << endl;
            cout << this->number << endl;
            cout << this->city << endl;
        }

        string get_street()
        {
            return this->street;
        }

        bool operator==(Address other)
        {
            if (other.street!=this->street) return false;
            return true;
        }
};

class House
{
    private:
        Address address;
        vector <Pet*> pets;
        
    public:
        House() {}
        House(Address address)
        {
            this->address = address;
        }

        void add_pets(Pet* pet)
        {
            pets.push_back(pet);
        }

        void remove_pets(Pet pet)
        {
            for (int i=0;i<pets.size();i++)
                {
                    Pet *n=dynamic_cast<Pet*>(this->pets[i]);
                    if (n)
                    {
                        if (*n==pet)
                            pets.erase(pets.begin()+i);
                    }
                }
        }

        void print_pets()
        {
            for(int i = 0; i < pets.size(); i++)
            {
                cout << pets[i]->get_name() << endl;
            }
        }

        string get_street()
        {
            return address.get_street();
        }


        int number_pets()
        {
            return pets.size();
        }
        
};

class Neighborhood
{
    private:
        vector <House*> list_of_house;
    
    public:
        void add_house(House* house)
        {
            list_of_house.push_back(house);
        }

        void count_pets_same_street(string street)
        {
            int sum = 0;
            for(int i = 0; i < list_of_house.size(); i++)
            {
                if(list_of_house[i]->get_street() == street)
                {
                    sum += list_of_house[i]->number_pets();
                }
            }

            cout << sum << endl;
        };
};

int main()
{
    Address A("abcd",100,"abc");
    House a(A);
    Pet *Thang = new Dog("Thang",19);
    Dog Quang("Quang",20);
    Dog Khang("Khang",69);
    Cat Huong("Huong",50);
    Pet *o = new Dog("QuangNet",20);
    
    o->make_noise();

    Address B("abcd",10,"abc");
    House b(B);

    Neighborhood s;

    a.add_pets(Thang);
    a.add_pets(&Quang);

    b.add_pets(&Khang);
    b.add_pets(&Huong);

    b.add_pets(o);

    vector<House*> list_of_house;
    s.add_house(&a);
    s.add_house(&b);

    s.count_pets_same_street("abcd");
    
} list_of_hou