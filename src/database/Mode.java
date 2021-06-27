package database;

public enum Mode
{
    READ
    {
        public boolean open()
        {
            return true;
        }

        public boolean create()
        {
            return false;
        }
    },

    OPEN
    {
        public boolean open()
        {
            return true;
        }

        public boolean create()
        {
            return false;
        }
    };

    public abstract boolean open();
    public abstract boolean create();
}
