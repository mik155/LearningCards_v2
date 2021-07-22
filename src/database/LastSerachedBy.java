package database;

public enum LastSerachedBy
{
    PREV
    {
        public boolean prev()
        {
            return true;
        }

        public boolean next()
        {
            return false;
        }
    },

    NEXT
    {
        public boolean prev()
        {
            return false;
        }

        public boolean next()
        {
            return true;
        }
    };
    public abstract boolean prev();
    public abstract boolean next();
}
