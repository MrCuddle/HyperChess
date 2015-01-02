package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 02/01/2015.
 */
public class Flag {

    Drawable shape;
    GamePiece holder;
    public int gridPosX, gridPosY;
    int initialPosX, initialPosY;

    public Flag(Context context, int x, int y){
        shape = context.getResources().getDrawable(R.drawable.flag_shape);
        initialPosX = gridPosX = x;
        initialPosY = gridPosY = y;
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize, gridPosY*GameBoard.TileSize + GameBoard.TileSize);
    }

    public void Update(double dt){
        if(holder != null){
            shape.setBounds(holder.GetPosition().x, holder.GetPosition().y, holder.GetPosition().x + 90, holder.GetPosition().y + 90);
        }
    }

    public void OnFlagDropped(){
        holder = null;
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize, gridPosY*GameBoard.TileSize + GameBoard.TileSize);
    }

    public void SetGridPosition(int x, int y){
        gridPosX = x;
        gridPosY = y;
    }

    public void SetHolder(GamePiece newHolder){
        holder = newHolder;
    }

    public void Draw(Canvas c){
        shape.draw(c);

    }

    public void ResetPosition(){
        shape.setBounds(initialPosX*GameBoard.TileSize, initialPosY*GameBoard.TileSize,
                initialPosX*GameBoard.TileSize + GameBoard.TileSize, initialPosY*GameBoard.TileSize + GameBoard.TileSize);
        holder = null;
    }

}
