package application;

import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

import javax.imageio.IIOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cal13 on 3/24/2017.
 */
public class User implements Serializable
{

    private String name;
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String name, String username, String password)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }
    public User(String name, String username, String password, boolean isAdmin)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }


    public String getName()
    {
        return this.name;
    }

    public boolean checkPassword(String password)
    {
        return (password.compareTo(this.password) == 0);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean isAdmin(){return this.isAdmin;}

    public static boolean doesUsernameExist(String username)
    {
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            ArrayList<User> uL = (ArrayList<User>) o.readObject();

            for (User x : uL)
            {
                if (x.getUsername().equalsIgnoreCase(username))
                    return true;
            }
            return false;
        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }

    }

    public static boolean checkUserAndPass(String username, String password)
    {
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            ArrayList<User> uL = (ArrayList<User>) o.readObject();

            for (User x : uL)
            {
                if (x.getUsername().equalsIgnoreCase(username) && x.getPassword().equals(password))
                {
                    return true;
                }
            }

            return false;
        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }
    }

    public static boolean commitUser(User u)
    {
        ArrayList<User> uL = new ArrayList<>();

        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            uL = (ArrayList<User>) o.readObject();


        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error pulling old Users");
        }

        uL.add(u);

        try
        {
            FileOutputStream fo = new FileOutputStream("Users.ser");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            oo.writeObject(uL);
            return true;

        }
        catch (IOException e)
        {
            return false;
        }

    }

    private String getPassword()
    {
        return this.password;
    }
}
